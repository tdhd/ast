#include "test.h"

int main(int *some_args) {
  int x = call_me() + 1;
  return x + some_args[0];
}

