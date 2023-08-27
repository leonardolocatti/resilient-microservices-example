import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        timeout: {
            executor: 'constant-arrival-rate',
            duration: '1m',
            rate: 11,
            timeUnit: '1s',
            preAllocatedVUs: 100
        }
    }
};

export default function () {
    const url = 'http://host.docker.internal:8001/service-a/greetings/ab';

    const res = http.get(url);

    check(res, { 'status was 200': (r) => r.status === 200 });
}