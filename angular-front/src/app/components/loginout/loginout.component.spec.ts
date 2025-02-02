import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginoutComponent } from './loginout.component';

describe('LoginoutComponent', () => {
  let component: LoginoutComponent;
  let fixture: ComponentFixture<LoginoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginoutComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
