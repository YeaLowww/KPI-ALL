import { Component, OnInit} from '@angular/core';
import { HttpService} from './http.service';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-root',
  template: `
  <ul>
  <li *ngFor="let user of users | async">
    <p>Ім’я користувача: {{user.name}}</p>
    <p>Вік користувача: {{user.age}}</p>
  </li>
  </ul>
  
  `,
  providers: [HttpService]
})
export class AppComponent implements OnInit {
  users: Observable<Object>|undefined;
  constructor(private httpService: HttpService){}
  ngOnInit(){
    this.users = this.httpService.getUsers();
  }
}