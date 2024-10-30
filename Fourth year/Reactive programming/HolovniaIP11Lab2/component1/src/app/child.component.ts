import { Input, Component, OnDestroy} from '@angular/core';
@Component({
    selector: 'child-comp',
    template: ` <ng-content></ng-content>
    <p>Привіт {{name}}</p>
    <p>Ім’я користувача: {{userName}}</p>
    <p>Вік користувача: {{userAge}}</p>`,
    styles: [`h3,p{color:red;}`]
})
export class ChildComponent implements OnDestroy{
    name = "Тарас";
    @Input() userName: string = "";
    @Input() userAge: number = 0;


    ngOnDestroy(): void {
        console.log('DESTROOOY!');
    }
}
