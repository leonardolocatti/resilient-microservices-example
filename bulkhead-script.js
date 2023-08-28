import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        bulkhead: {
            executor: 'constant-arrival-rate',
            duration: '1m',
            rate: 11,
            timeUnit: '1s',
            preAllocatedVUs: 100
        }
    }
};

export default function () {
    const responses = http.batch([
        ['GET', 'http://host.docker.internal:8001/service-a/congratulations/ab', null, {timeout: 3000}],
        ['GET', 'http://host.docker.internal:8001/service-a/congratulations/ac', null, {timeout: 3000}],
      ]);

      check(responses[1], {
        'status was 200': (res) => res.status === 200,
      });
}