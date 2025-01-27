import { Component, Input } from '@angular/core';
import {NgIf, NgForOf, DatePipe} from '@angular/common';
import { Article } from '../../../../services/generated';

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.scss'],
  imports: [NgIf, NgForOf, DatePipe],
  standalone: true,
})
export class SearchResultsComponent {
  @Input() articles: Article[] = [];

  // Helper to generate favicon URLs (if needed)
  faviconUrl(feedUrl: string): string {
    const domain = new URL(feedUrl).hostname;
    return `https://www.google.com/s2/favicons?domain=${domain}`;
  }
}
