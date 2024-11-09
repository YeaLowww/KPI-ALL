import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable()
export class HttpService {
    constructor(private http: HttpClient) { }
    getUsers(): Observable<Object[]> {
        return this.http.get<Object[]>('assets/users.json');
      }
}
