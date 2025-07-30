import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationEditDialogComponent } from './reservation-edit-dialog.component';

describe('ReservationEditDialogComponent', () => {
  let component: ReservationEditDialogComponent;
  let fixture: ComponentFixture<ReservationEditDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReservationEditDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationEditDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
