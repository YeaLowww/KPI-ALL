import { Component, OnDestroy } from '@angular/core';
@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnDestroy {
    name:string = 'Петро';
    name2:string="Tom";
    age:number = 24;
    showComponent: boolean = true;
    
    ngOnDestroy(): void {
        console.log('DESTROOOY in APP!');
    }
    

}

