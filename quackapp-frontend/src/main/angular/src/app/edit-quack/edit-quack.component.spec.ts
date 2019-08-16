import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditQuackComponent } from './edit-quack.component';

describe('EditQuackComponent', () => {
  let component: EditQuackComponent;
  let fixture: ComponentFixture<EditQuackComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditQuackComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditQuackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
