import { Component } from '@angular/core';
import {SearchService} from "../services/search.service"
import {FormsModule} from '@angular/forms';
import {SearchResults} from "../model";


@Component({
  selector: 'app-search',
  standalone: true,
  templateUrl: './search.component.html',
  imports: [FormsModule],
})
export class SearchComponent {
  searchTerm: string = '';
  results: SearchResults | null = null;

  constructor(private searchService: SearchService) {}

  onSearch(): void {
    if (this.searchTerm.trim()) {
      this.searchService.search(this.searchTerm).subscribe({
        next: (data: SearchResults) => {
          this.results = data;
        },
        error: (error) => {
          console.error('Error fetching search results', error);
        }
      });
    }
  }
}
