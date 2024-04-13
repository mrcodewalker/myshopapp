export class LoginGoogleDto{
  email: string;
  picture: string;
  name: string;

  constructor(data: any) {
    this.name = data.name;
    this.picture = data.picture;
    this.email = data.email;
  }
}
