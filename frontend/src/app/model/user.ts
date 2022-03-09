export class User {
  id: number = 0;
  username: string = '';
  password: string = '';
  activation: boolean = false;


  constructor(user: User) {
    this.id = user.id;
    this.username = user.username;
    this.password = user.password;
    this.activation = user.activation;
  }
}
