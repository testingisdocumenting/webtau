import { configure } from '@storybook/react';

function loadStories() {
  require('../src/report');
}

configure(loadStories, module);
