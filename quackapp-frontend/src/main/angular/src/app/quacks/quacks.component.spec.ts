import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuacksComponent } from './quacks.component';

describe('QuacksComponent', () => {
  let component: QuacksComponent;
  let fixture: ComponentFixture<QuacksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QuacksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QuacksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
