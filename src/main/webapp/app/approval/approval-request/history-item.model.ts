import { Moment } from 'moment';

export interface IHistoryItem {
  id?: number;
  actUserId?: string;
  actUserDisplayName?: string;
  actionName?: string;
  comment?: string;
  oldRequestStatus?: string;
  newequestStatus?: string;
  actionDate?: Moment;
}
export class HistoryItem implements IHistoryItem {
  constructor(
    public id?: number,
    public actUserId?: string,
    public actUserDisplayName?: string,
    public actionName?: string,
    public comment?: string,
    public oldRequestStatus?: string,
    public newequestStatus?: string,
    public actionDate?: Moment
  ) {}
}
