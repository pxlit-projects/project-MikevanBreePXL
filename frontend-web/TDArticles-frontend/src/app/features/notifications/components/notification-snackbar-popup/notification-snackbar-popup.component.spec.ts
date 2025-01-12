import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationSnackbarPopupComponent } from './notification-snackbar-popup.component';

describe('NotificationSnackbarPopupComponent', () => {
  let component: NotificationSnackbarPopupComponent;
  let fixture: ComponentFixture<NotificationSnackbarPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificationSnackbarPopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificationSnackbarPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
