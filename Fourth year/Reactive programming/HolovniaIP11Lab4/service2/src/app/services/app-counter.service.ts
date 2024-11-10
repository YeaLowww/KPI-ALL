import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AppCounterService {
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