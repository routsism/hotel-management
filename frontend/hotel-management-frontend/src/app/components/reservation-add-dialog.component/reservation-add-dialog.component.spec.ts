import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationAddDialogComponent } from './reservation-add-dialog.component';

describe('ReservationAddDialogComponent', () => {
  let component: ReservationAddDialogComponent;
  let fixture: ComponentFixture<ReservationAddDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReservationAddDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationAddDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
