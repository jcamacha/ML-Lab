export interface Dataset {
  datasetId: number;
  name: string;
  description: string;
  datasetType: string;
  numSamples: number;
  numFeatures: number;
  targetVariable: string;
  createdAt: string;
}
