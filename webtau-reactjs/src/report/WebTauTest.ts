export interface WebTauTest {
  id: string;
  steps: WebTauStep[];
}

export interface WebTauStep {
  children?: WebTauStep[];
  message: TokenizedMessageToken[];
  elapsedTime: number;
  startTime: number;
  personaId?: string;
  input?: WebTauStepInput;
}

export interface TokenizedMessageToken {
  type: string;
  value: any;
}

export interface WebTauStepInput {
  type: string;
  data: any;
}

export type WebTauStepInputKeyValue = { [key: string]: string };
