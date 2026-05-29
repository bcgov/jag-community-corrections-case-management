# Form Autosave and Submission Enhancements Spec

## Purpose
This specification defines the remaining enhancements after the data-loss prevention patch set (top-priority items 1-3) to improve reliability, user trust, and diagnosability of form editing and submission flows.

## Context
The application uses Form.io for rendering and a custom autosave store for persistence. The first patch set addressed:
- autosave payload re-queue bug after retry failures,
- forced autosave flush before high-risk navigation paths,
- Save and Continue behavior updated to flush pending changes before section navigation.

This spec covers all remaining recommendations (previously listed as items 4-14).

## Goals
- Align user actions with predictable save semantics.
- Ensure final submit uses latest persisted values and explicit validation rules.
- Provide clear save-state feedback and actionable failure UX.
- Improve observability to support incident analysis.
- Add automated test coverage for race conditions and failure paths.

## Non-Goals
- Backend schema redesign.
- Replacing Form.io.
- Major visual redesign unrelated to save reliability.

## In Scope
1. Save and submit semantics cleanup.
2. Validation hardening for final submit.
3. Save-state and failure user feedback.
4. Navigation guards for unsaved changes.
5. Auth-refresh failure handling in autosave lifecycle.
6. Telemetry and logging improvements.
7. Unit and E2E test additions.

## Out of Scope
- New roles/permissions model.
- API endpoint contract changes that require backend migration.

## Functional Requirements

### FR-1: Separate Save and Continue from Submit semantics
- Save and Continue must always:
  - flush pending autosave changes,
  - block navigation on failure,
  - navigate to next section only when flush succeeds.
- Submit must always:
  - flush pending changes,
  - run full validation path,
  - invoke complete endpoint only when validation succeeds.

### FR-2: Explicit pre-submit validation checkpoint
- Introduce explicit validation step before complete API call.
- Validation failure must:
  - prevent completion call,
  - show field-level and summary error details,
  - keep user on current form state.
- If backend validation remains authoritative, frontend must still flush latest pending edits before invoking complete.

### FR-3: Remove hidden-button dependency for data collection (preferred)
- Replace hidden submit event-trigger pattern with direct Form.io instance submission retrieval and validation invocation.
- Acceptance fallback: if hidden event remains temporarily, document known limitations and add tests around it.

### FR-4: Save-state indicator
- Introduce persistent save indicator in form UI with states:
  - Idle,
  - Saving,
  - Saved (timestamp),
  - Retrying,
  - Failed (changes pending).
- Indicator must update based on autosave store state transitions.

### FR-5: Sticky failure banner and retry path
- On autosave failure, show persistent inline warning banner with:
  - concise message,
  - retry action,
  - optional detail toggle.
- Toasts can remain but are not sufficient alone.

### FR-6: Navigation guard for unsaved changes
- For tab switches and route exits while pending changes exist:
  - disable or block navigation while saving, or
  - show confirmation dialog with options:
    - Wait for save,
    - Leave without saving,
    - Cancel.
- Default action should favor preventing data loss.

### FR-7: Token refresh failure as explicit save failure
- If request interceptor token refresh fails during autosave:
  - mark autosave as failed with pending changes retained,
  - present blocking warning before navigation,
  - avoid silent state loss.

### FR-8: Telemetry events
Emit structured events for:
- autosave_started,
- autosave_succeeded,
- autosave_failed,
- autosave_retried,
- autosave_dropped (should be 0 target),
- navigation_with_pending_changes,
- submit_validation_failed,
- submit_completed.

Event payload fields:
- clientFormId,
- clientNumber,
- formType,
- sectionKey/questionKey (when available),
- retryCount,
- latencyMs,
- transactionId,
- errorType/errorCode.

### FR-9: Correlation ID surfacing
- Surface request correlation/transaction ID in user-visible failure details for support triage.
- Maintain backend parity with existing X-Transaction-Id headers.

### FR-10: Automated test coverage

#### Unit tests
- Autosave store state machine:
  - queueing, flush, retry, merge-back, pending checks.
- Validation gate behavior before complete call.
- Save-state status transitions.

#### E2E tests
- Textarea input without manual blur then Save and Continue.
- Fast tab/form switching during save.
- API 500 on autosave with retry and pending preservation.
- Token-refresh failure during autosave.
- Final submit with validation error; ensure no completion call.

## Suggested Implementation Plan

### Phase A: UX and behavior alignment
- Implement FR-1, FR-4, FR-5.
- Add minimal route/tab guard hooks from FR-6.

### Phase B: submit/validation hardening
- Implement FR-2 and FR-3.
- Add submit-specific telemetry events.

### Phase C: auth and observability
- Implement FR-7, FR-8, FR-9.
- Add dashboards/queries for failure rate and retry metrics.

### Phase D: test completion
- Add FR-10 unit and E2E suites.
- Gate merges on save/submit reliability test suite.

## Target File Areas
- src/stores/autoSaveStore.js
- src/components/form/FormRenderer.vue
- src/components/common/FormioButtonGroupSubmit.vue
- src/components/form/formSections/FormDataEntry.vue
- src/components/CmpForm.vue
- src/services/setupAxioInterceptors.ts
- src/middleware/update-token.ts
- tests/unit/* (new)
- tests/e2e/* (new)

## Acceptance Criteria
1. No reproducible data loss in textarea-no-blur + Save and Continue scenario.
2. Form/tab switch cannot silently discard pending edits.
3. Submit cannot complete with unsaved pending changes.
4. Validation failures are visible and block completion.
5. Save status is always visible and accurate.
6. Autosave failure leaves data pending and recoverable.
7. Telemetry can identify top failure modes by form type and endpoint.
8. Automated tests cover at least the five highest-risk flows.

## Operational Metrics
Track weekly:
- autosave failure rate,
- autosave retry success rate,
- completion failure rate due to validation,
- navigation attempts with pending changes,
- user-reported data-loss incidents.

Success threshold after release:
- data-loss incidents reduced to near zero,
- autosave failure recovery > 95% within retry policy,
- no silent discard paths in test matrix.

## Open Questions
1. Should navigation be hard-blocked or user-confirmed when pending changes exist?
2. Should Save and Continue display a short synchronous loading state before advancing?
3. Should final submit call dedicated validate endpoints before complete endpoint?
4. Is offline/local-draft support desired for long editing sessions?

## Rollout Strategy
- Feature flag for new save-state UI and nav guard behavior.
- Canary rollout for selected user group.
- Monitor telemetry and support tickets for 1-2 weeks before full rollout.

## Handoff Notes For Next Chat Session
Use this implementation order:
1. FR-4 and FR-5 first (immediate user visibility).
2. FR-1 and FR-2 second (behavior correctness).
3. FR-6 and FR-7 third (guardrails).
4. FR-8/FR-9 observability and FR-10 tests last, then harden.

Ask for backend confirmation on validation endpoint usage before implementing FR-3 replacement of hidden-submit flow.
