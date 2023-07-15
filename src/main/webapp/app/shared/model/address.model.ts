export interface IAddress {
  id?: number;
  unitNo?: string;
  street?: string;
  city?: string;
  country?: string;
  postcode?: string;
  customerName?: string;
  customerId?: number;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public unitNo?: string,
    public street?: string,
    public city?: string,
    public country?: string,
    public postcode?: string,
    public customerName?: string,
    public customerId?: number
  ) {}
}
