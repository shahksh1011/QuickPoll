export class User {
  id: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  active: bigint;
  phoneNumber: string;
  token?: string;
}
