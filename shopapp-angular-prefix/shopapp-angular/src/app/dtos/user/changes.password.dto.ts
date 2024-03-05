export class ChangesPasswordDto{
  old_password: string;
  password: string;
  retype_password: string;

  constructor(data: any) {
    this.password = data.password;
    this.old_password = data.old_password;
    this.retype_password = data.retype_password;
  }
}
