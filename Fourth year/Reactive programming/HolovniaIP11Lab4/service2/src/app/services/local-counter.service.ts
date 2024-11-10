import { Injectable } from '@angular/core';

@Injectable()
export class LocalCounterService {
  counter: number = 0;

  increment() {
    this.counter++;
  }

  decrement() {
    this.counter--;
  }

  getCounter() {
    return this.counter;
  }
}