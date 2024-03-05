export class UpdateUserByAdminDto{
  role_name: String;
  is_active: boolean;

  constructor(data: any) {
    this.role_name = data.role_name;
    this.is_active = data.is_active;
  }
}
