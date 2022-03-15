export class Employee {
  id: number = 0;
  officeId: number = 0;
  name: string = '';
  position: string = '';
  email: string = '';

  constructor(employee: Employee) {
    this.id = employee.id;
    this.officeId = employee.officeId;
    this.name = employee.name;
    this.position = employee.position;
    this.email = employee.email;
  }

  public toString = () => {
    return this.id + '/' + this.name + '/' + this.officeId + '/' + this.position + '/' + this.email;
  }

}


