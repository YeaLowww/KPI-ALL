import { Component } from '@angular/core';
import { AppCounterService } from '../services/app-counter.service';
import { LocalCounterService } from '../services/local-counter.service';

@Component({
  selector: 'app-counter',
  template: `
    <h2>
      Сервіс верхнього рівня App Counter {{ appCounterService.counter }}
    </h2>
    <button class="btn" (click)="appCounterService.increment()">+</button>
    <button class="btn" (click)="appCounterService.decrement()">-</button>
    <h2>
      Сервіс рівня компоненту Local Counter {{ localCounterService.counter }}
    </h2>
    <button class="btn" (click)="localCounterService.increment()">+</button>
    <button class="btn" (click)="localCounterService.decrement()">-</button>
    <hr/>
  `,
  providers: [LocalCounterService],
})
export class CounterComponent {
  constructor(
    public appCounterService: AppCounterService,
    public localCounterService: LocalCounterService
  ) {}
  // counter: number = 0;

  // increment() {
  //   this.counter++;
  // }

  // decrement() {
  //   this.counter--;
  // }
}