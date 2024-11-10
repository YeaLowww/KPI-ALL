import { Component } from '@angular/core';
import { LocalCounterService } from './services/local-counter.service';
@Component({
  selector: 'my-app',
  template: `
    <h1>Компонент app.component.ts</h1>
    <app-counter></app-counter>
    <hr />
    <h1>Компонент counter.component.ts</h1>
    <app-counter></app-counter>
  `,
  providers: [LocalCounterService],
})
export class AppComponent {
  constructor(public localCounterService: LocalCounterService) {}
}