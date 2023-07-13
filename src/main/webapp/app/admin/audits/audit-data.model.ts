export class AuditData {
  constructor(public remoteAddress: string, public sessionId: string, public messageKey: string, public messageValue: string) {}
}
