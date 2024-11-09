import { Component } from '@angular/core';
export interface Post {
  title:string;
  text:string;
  id?:number;
  date: Date;
}
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  posts: Post[] = [
    { title: 'Вивчаю компоненти', text: 'Створюю проект "Блог"', id: 0, date: new Date() },
    { title: 'Вивчаю директиви', text: 'Все ще створюю "Блог"', id: 1, date: new Date() }]

  search: string = '';

  counter = this.posts.length;

  updatePosts(post:Post){
    post.id = this.counter++; 
    this.posts.unshift(post);
    console.log('Post',post);
  }
  deletePost(id:number){
    this.posts=this.posts.filter(p=>p.id!==id)
  }
}
