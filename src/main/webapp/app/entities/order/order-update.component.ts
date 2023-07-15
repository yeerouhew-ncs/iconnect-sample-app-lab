import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IOrder, Order } from 'app/shared/model/order.model';
import { OrderService } from './order.service';

type SelectableEntity = IOrder;

@Component({
  selector: 'ic-order-update',
  templateUrl: './order-update.component.html'
})
export class OrderUpdateComponent implements OnInit {
  private order: IOrder;
  isSaving: Boolean;
  orderDateDp: any;

  editForm = this.fb.group({
    id: [],
    product: [null, [Validators.required]],
    quantity: [null, [Validators.required]],
    price: [null, [Validators.required]],
    payment: [null, [Validators.required]],
    orderDate: [null, [Validators.required]],
    remarks: [null, [Validators.required]]
  });

  constructor(protected orderService: OrderService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.updateForm(order);
    });
  }

  updateForm(order: IOrder): void {
    this.editForm.patchValue({
      id: order.id,
      product: order.product,
      quantity: order.quantity,
      price: order.price,
      payment: order.payment,
      orderDate: order.orderDate,
      remarks: order.remarks
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.createFromForm();
    if (order.id !== undefined) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  private createFromForm(): IOrder {
    return {
      ...new Order(),
      id: this.editForm.get(['id'])!.value,
      product: this.editForm.get(['product'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      price: this.editForm.get(['price'])!.value,
      payment: this.editForm.get(['payment'])!.value,
      orderDate: this.editForm.get(['orderDate'])!.value,
      remarks: this.editForm.get(['remarks'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
