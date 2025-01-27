import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-search-bar',
  template: `
    <div class="search-container">
      <div class="search-bar">
        <div class="icon-container">
          <img
            ngSrc="/assets/search_icon.svg"
            alt="Search icon"
            width="24"
            height="24"
          />
        </div>
        <input
          class="search-input"
          type="text"
          placeholder="Search articles..."
          [(ngModel)]="searchQuery"
          (keyup.enter)="emitSearch()"
        />
      </div>
    </div>
  `,
  styleUrls: ['./search-bar.component.scss'],
  imports: [FormsModule, NgOptimizedImage],
})
export class SearchBarComponent {
  @Output() search = new EventEmitter<string>();
  searchQuery = '';

  emitSearch() {
    this.search.emit(this.searchQuery.trim());
  }
}
