#include "platform.h"

namespace ch {

// Audio backend hook: playback is wired up with the sound milestone; until
// then the player only tracks the MIDP state machine (300 prefetched, 400 started).
void Player::start() { state_ = 400; }
void Player::stop() { state_ = 300; }
void Player::close() { state_ = 0; }

}  // namespace ch
