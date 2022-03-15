interface Alert {
  type: string;
  message: string;
}

export class HaveAlert {
  alerts: Alert[] = [];

  closeAlert(alert: Alert) {
    this.alerts.splice(this.alerts.indexOf(alert), 1);
  }
}
