export class LoginFacebookDto{
  email: string;
  facebook_id: string;
  name: string;

  constructor(data: any) {
    this.name = data.name;
    this.facebook_id = data.facebook_id;
    this.email = data.email;
  }
}
