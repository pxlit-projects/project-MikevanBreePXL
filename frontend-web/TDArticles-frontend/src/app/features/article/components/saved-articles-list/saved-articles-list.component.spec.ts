import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedArticlesListComponent } from './saved-articles-list.component';

describe('SavedArticlesListComponent', () => {
  let component: SavedArticlesListComponent;
  let fixture: ComponentFixture<SavedArticlesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SavedArticlesListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SavedArticlesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
