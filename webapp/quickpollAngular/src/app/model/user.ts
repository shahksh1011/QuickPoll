export class User {
  id: bigint;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  active: bigint;
  phoneNumber: string;
  token?: string;
}
