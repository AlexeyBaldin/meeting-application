export class Hasher {

  private constructor() {
  }

  public static hashPassword(password: string): string {
    let hash = 0;
    if (password == '') console.log(hash);

    for (let i = 0; i < password.length; i++) {
      let char = password.charCodeAt(i);
      console.log(char + ' ' + hash);
      hash = ((hash << 5) - hash) + char;
    }

    return hash.toString();
  }
}
