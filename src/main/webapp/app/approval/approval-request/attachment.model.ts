export class Attachment {
  constructor(
    public id?: number,
    public approvalRequestFieldKey?: string,
    public fileName?: string,
    public fileSize?: number,
    public fileType?: string,
    public file?: any
  ) {}
}
