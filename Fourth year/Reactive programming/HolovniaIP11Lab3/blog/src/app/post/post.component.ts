import { Component, Input, OnDestroy, EventEmitter, Output, OnInit } from '@angular/core';
import { Post } from '../app.component';
@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnDestroy {
  @Input('toPost') myPost!: Post;
  
  constructor() { }
  
  ngOnDestroy() {
    console.log('метод ngOnDestroy');
  }
  
  @Output() onRemove=new EventEmitter<number>()
  removePost(){
    this.onRemove.emit(this.myPost.id)
    
  }

}
