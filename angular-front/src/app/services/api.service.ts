import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Training } from '../model/training.model';
import { User } from '../model/user.model';
import { Observable } from 'rxjs';
import { Category } from '../model/category.model';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  public getTrainings() {
    return this.http.get<Training[]>(environment.host + "/trainings");
  }

  public getTraining(id: number) {
    return this.http.get<Training>(environment.host + "/trainings/" + id);
  }

  public getUsers() {
    return this.http.get<User[]>(environment.host + "/users");
  }

  public getUserByEmail(email: string) {
    return this.http.get<User[]>(environment.host + "/users?email=" + email);
  }

  public postUser(user: any) {
    return this.http.post<User>(environment.host + "/users", user);
  }

  public postTraining(training: any) {
    return this.http.post<Training>(environment.host + "/trainings", training);
  }

  public getUserByEmailAndPassword(email: string, password: string) {
    return this.http.get<User>(`${environment.host}/users?email=${email}&password=${password}`);
  }

  public delTraining(training: Training) {
    return this.http.delete(environment.host + "/trainings/" + training.id);
  }

  public putTraining(training: any) {
    return this.http.put<Training>(environment.host + "/trainings/" + training.id, training);
  }

  /* public updateTraining( training: Training):Observable<Training>{
   
   const url = `${environment.host}/trainings/${training.id}`;
   return this.http.put<Training>(url, training);
 } */


  public getCategories() {
    return this.http.get<Category[]>(environment.host + "/categories");
  }

  updateTraining(id: number, traingsDetail: any): Observable<any> {
    const url = `${environment.host}/trainings/${id}`;
    return this.http.put(url, traingsDetail);
  }

  public login(username: string, password: string): Observable<any> {
    return this.http.post<{ "access-token": string }>(
      `${environment.host}/login`,
      { username, password }
    );
  }

}
