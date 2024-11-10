import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { AppCounterService } from './services/app-counter.service';
import { LocalCounterService } from './services/local-counter.service';
import { CounterComponent } from './counter/counter.component';
@NgModule({
  imports: [BrowserModule, FormsModule],
  declarations: [AppComponent, CounterComponent],
  providers: [AppCounterService, LocalCounterService],
  bootstrap: [AppComponent],
})
export class AppModule {}