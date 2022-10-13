# Reactive Preferences Toolbox

A collection of libraries to allow `SharedPreferences` to be reactive

This library was created to continue improving on the great work that [@f2prateek](https://github.com/f2prateek) had done with [rx-preferences](https://github.com/f2prateek/rx-preferences).

In order to handle the varying number of reactive frameworks available, there are multiple libraries available, which all share the same basic APIs and adapters.

## Available Reactive SharedPreferences Libraries

- [Coroutine/Flow](./coroutine/README.md)
  - `implementation 'com.frybits.reactive-preferences:coroutine:0.0.1'`
- [LiveData](./livedata/README.md)
  - `implementation 'com.frybits.reactive-preferences:livedata:0.0.1'`
- [Rx2](./rx2/README.md)
  - `implementation 'com.frybits.reactive-preferences:rx2:0.0.1'`
- [Rx3](./rx3/README.md)
  - `implementation 'com.frybits.reactive-preferences:rx3:0.0.1'`

License
-------
```
   Copyright 2022 Pablo Baxter

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```