import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptArticlesListComponent } from './concept-articles-list.component';

describe('ConceptArticlesListComponent', () => {
  let component: ConceptArticlesListComponent;
  let fixture: ComponentFixture<ConceptArticlesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConceptArticlesListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConceptArticlesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
