import {
  IsString,
  IsPhoneNumber, IsNotEmpty, IsNumber
} from 'class-validator';

export class LoginDto {
  @IsPhoneNumber()
  @IsNotEmpty()
  phone_number: string;

  @IsString()
  @IsNotEmpty()
  password: string;

  @IsString()
  email: string;

  constructor(data: any) {
    this.phone_number = data.phone_number;
    this.password = data.password;
    this.email = data.email;
  }
}
