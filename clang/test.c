#include "test.h"


void fsstack_copy_inode_size(struct inode *dst, struct inode *src)
{
        loff_t i_size;
        blkcnt_t i_blocks;

        /*
         * i_size_read() includes its own seqlocking and protection from
         * preemption (see include/linux/fs.h): we need nothing extra for
         * that here, and prefer to avoid nesting locks than attempt to keep
         * i_size and i_blocks in sync together.
         */
        i_size = i_size_read(src);

        /*
         * But if CONFIG_LBDAF (on 32-bit), we ought to make an effort to
         * keep the two halves of i_blocks in sync despite SMP or PREEMPT -
         * though stat's generic_fillattr() doesn't bother, and we won't be
         * applying quotas (where i_blocks does become important) at the
         * upper level.
         *
         * We don't actually know what locking is used at the lower level;
         * but if it's a filesystem that supports quotas, it will be using
         * i_lock as in inode_add_bytes().
         */
        if (sizeof(i_blocks) > sizeof(long))
                spin_lock(&src->i_lock);
        i_blocks = src->i_blocks;
        if (sizeof(i_blocks) > sizeof(long))
                spin_unlock(&src->i_lock);

        /*
         * If CONFIG_SMP or CONFIG_PREEMPT on 32-bit, it's vital for
         * fsstack_copy_inode_size() to hold some lock around
         * i_size_write(), otherwise i_size_read() may spin forever (see
         * include/linux/fs.h).  We don't necessarily hold i_mutex when this
         * is called, so take i_lock for that case.
         *
         * And if CONFIG_LBDAF (on 32-bit), continue our effort to keep the
         * two halves of i_blocks in sync despite SMP or PREEMPT: use i_lock
         * for that case too, and do both at once by combining the tests.
         *
         * There is none of this locking overhead in the 64-bit case.
         */
        if (sizeof(i_size) > sizeof(long) || sizeof(i_blocks) > sizeof(long))
                spin_lock(&dst->i_lock);
        i_size_write(dst, i_size);
        dst->i_blocks = i_blocks;
        if (sizeof(i_size) > sizeof(long) || sizeof(i_blocks) > sizeof(long))
                spin_unlock(&dst->i_lock);
}


void deactivate_locked_super(struct super_block *s)
{
        struct file_system_type *fs = s->s_type;
        if (atomic_dec_and_test(&s->s_active)) {
                cleancache_invalidate_fs(s);
                unregister_shrinker(&s->s_shrink);
                fs->kill_sb(s);

                /*
                 * Since list_lru_destroy() may sleep, we cannot call it from
                 * put_super(), where we hold the sb_lock. Therefore we destroy
                 * the lru lists right now.
                 */
                list_lru_destroy(&s->s_dentry_lru);
                list_lru_destroy(&s->s_inode_lru);

                put_filesystem(fs);
                put_super(s);
        } else {
                up_write(&s->s_umount);
        }
}

int thaw_super(struct super_block *sb)
{
        int error;

        down_write(&sb->s_umount);
        if (sb->s_writers.frozen != SB_FREEZE_COMPLETE) {
                up_write(&sb->s_umount);
                return -EINVAL;
        }

        if (sb_rdonly(sb)) {
                sb->s_writers.frozen = SB_UNFROZEN;
                goto out;
        }

        lockdep_sb_freeze_acquire(sb);

        if (sb->s_op->unfreeze_fs) {
                error = sb->s_op->unfreeze_fs(sb);
                if (error) {
                        printk(KERN_ERR
                                "VFS:Filesystem thaw failed\n");
                        lockdep_sb_freeze_release(sb);
                        up_write(&sb->s_umount);
                        return error;
                }
        }

        sb->s_writers.frozen = SB_UNFROZEN;
        sb_freeze_unlock(sb);
out:
        wake_up(&sb->s_writers.wait_unfrozen);
        deactivate_locked_super(sb);
        return 0;
}


int thaw_super_(struct super_block *sb)
{
        int error;

        down_write(&sb->s_umount);
        if (sb->s_writers.frozen != SB_FREEZE_COMPLETE) {
                up_write(&sb->s_umount);
                return -EINVAL;
        }

        if (sb_rdonly(sb)) {
                sb->s_writers.frozen = SB_UNFROZEN;
                goto out;
        }

        lockdep_sb_freeze_acquire(sb);

        if (sb->s_op->unfreeze_fs) {
                error = sb->s_op->unfreeze_fs(sb);
                if (error) {
                        printk(KERN_ERR
                                "VFS:Filesystem thaw failed\n");
                        lockdep_sb_freeze_release(sb);
                        up_write(&sb->s_umount);
                        return error;
                }
        }

        sb->s_writers.frozen = SB_UNFROZEN;
        sb_freeze_unlock(sb);
out:
        wake_up(&sb->s_writers.wait_unfrozen);
        deactivate_locked_super(sb);
        return 0;
}
