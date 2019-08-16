import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteQuackComponent } from './delete-quack.component';

describe('DeleteQuackComponent', () => {
  let component: DeleteQuackComponent;
  let fixture: ComponentFixture<DeleteQuackComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteQuackComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteQuackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
