import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        allEndpoints: {
            executor: 'constant-arrival-rate',
            duration: '5m',
            rate: 2,
            timeUnit: '1s',
            preAllocatedVUs: 10
        }
    }
};

export default function () {
    const urls = [
        'http://host.docker.internal:8001/greetings/a',
        'http://host.docker.internal:8001/greetings/ab',
        'http://host.docker.internal:8001/greetings/ac',
        'http://host.docker.internal:8001/greetings/abc',
        'http://host.docker.internal:8002/greetings/b',
        'http://host.docker.internal:8002/greetings/bc',
        'http://host.docker.internal:8003/greetings/c',
    ];

    urls.forEach(url => http.get(url));
}
