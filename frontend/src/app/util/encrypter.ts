export class Encrypter {

  public static encryptPassword(password: string): string {
    let stringBuilder: string = '';

    for (let i = 0; i < password.length; i++) {
      let char = password.charCodeAt(i);
      stringBuilder += String(char + i);
    }

    return stringBuilder;
  }
}
