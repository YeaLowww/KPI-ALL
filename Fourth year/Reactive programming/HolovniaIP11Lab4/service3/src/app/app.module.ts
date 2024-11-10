import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { UsersComponent } from './users/users.component';

@NgModule({
  imports: [BrowserModule, HttpClientModule],
  providers: [],
  declarations: [AppComponent, UsersComponent],
  bootstrap: [AppComponent],
})
export class AppModule {}