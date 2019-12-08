export class User {
  guid: string;
  customerUid: string;

  firstName: string;
  lastName: string;

  email: string;
  zipCode: string;

  password: string;

  constructor(obj: any = null) {
    if (obj != null) {
      Object.assign(this, obj);
    }
  }
}
