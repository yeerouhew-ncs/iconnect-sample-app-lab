import * as moment from 'moment';

export interface IOrder {
  id?: number;
  product?: string;
  quantity?: number;
  price?: number;
  payment?: string;
  orderDate?: moment.Moment;
  remarks?: string;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public product?: string,
    public quantity?: number,
    public price?: number,
    public payment?: string,
    public orderDate?: moment.Moment,
    public remarks?: string
  ) {}
}
