export class UpdatedUserDto{
  fullname: string;
  address: string;
  date_of_birth: Date;

  constructor(data: any) {
   this.fullname = data.fullname;
   this.address = data.address;
   this.date_of_birth = data.date_of_birth;
  }
}
